package bullish.electronic.store;

import bullish.electronic.store.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

/**
 * Here we start whole spring app with fully loaded context using SpringBootTest annotation
 * use it only for integration tests, when you need to test system end-to-end
 * for component test, use mocks, without starting full context
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class IntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void endToEndIntegrationTest() throws Exception {
        MvcResult result;

        result = mvc.perform(MockMvcRequestBuilders.post("/admin/product")
                .content("{\"name\":\"spoon\",\"weight\":100,\"quantity\":10}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spoon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.weight").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10))
                .andReturn();
        final Product product1 = TestHelper.jsonToObj(result.getResponse().getContentAsString(), Product.class);
        result = mvc.perform(MockMvcRequestBuilders.post("/admin/product")
                .content("{\"name\":\"fork\",\"weight\":100,\"quantity\":10}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("fork"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.weight").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10))
                .andReturn();
        final Product product2 = TestHelper.jsonToObj(result.getResponse().getContentAsString(), Product.class);

        mvc.perform(MockMvcRequestBuilders.post("/admin/product/" + product1.getId() + "/price")
                .content("{\"price\":50}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(50));
        mvc.perform(MockMvcRequestBuilders.post("/admin/product/" + product2.getId() + "/price")
                .content("{\"price\":100}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100));

        mvc.perform(MockMvcRequestBuilders.post("/admin/deal")
                .content("{\"name\":\"Get 50% off for second item\",\"dealClassName\":\"Get50PercentOffForSecondItem\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dealClassName").value("Get50PercentOffForSecondItem"));

        mvc.perform(MockMvcRequestBuilders.post("/user/cart/product")
                .content("{\"userId\":10,\"productId\":"+product1.getId()+",\"quantity\":5}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(product1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(5));
        mvc.perform(MockMvcRequestBuilders.post("/user/cart/product")
                .content("{\"userId\":10,\"productId\":"+product2.getId()+",\"quantity\":5}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(product2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(5));

        mvc.perform(MockMvcRequestBuilders.post("/user/cart/receipt")
                .content("{\"userId\":10}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(600));
    }

    @Test
    public void aspectExecutionTimeLoggingTest(CapturedOutput output) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/admin/product")
                .content("{\"name\":\"spoon\",\"weight\":100,\"quantity\":10}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("spoon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.weight").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10))
                .andReturn();
        Assert.isTrue(output.getAll().contains("Execution time of ProductController.createProduct"), "Missing execution time aspect logging");
    }
}