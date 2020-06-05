package com.dbtaxi;

import com.dbtaxi.model.enumStatus.DriverCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DbtaxiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }


    @Test
    void mainOperator() throws Exception {
        mockMvc.perform(get("/operator")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/mainOperator"));
    }

    @Test
    void findDrivers() throws Exception {
        mockMvc.perform(get("/operator/findDrivers")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/findDrivers"));
    }


    @Test
    void freeDrivers() throws Exception {
        mockMvc.perform(post("/operator/freeDrivers")
                .with(user("o1").password("o1").roles("OPERATOR"))
                .with(csrf())
                .param("category", DriverCategory.ECONOMY.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/freeDriversByCategory"));
    }

    @Test
    void addUnprocessedOrder() throws Exception {
        mockMvc.perform(post("/operator/addUnprocessedOrder")
                .with(user("o1").password("o1").roles("OPERATOR"))
                .with(csrf())
                .param("idDriver", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/operator"));
    }

    @Test
    void answerDriver() throws Exception {
        mockMvc.perform(get("/operator/answerDriver")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/answerDriver"));
    }


    @Test
    void currentOrders() throws Exception {
        mockMvc.perform(get("/operator/currentOrders")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/currentOrders"));
    }

    @Test
    void showComplaintsFromPassengers() throws Exception {
        mockMvc.perform(get("/operator/showComplaintsFromPassengers")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/showComplaintsFromPassengers"));
    }

    @Test
    void showComplaintsFromDrivers() throws Exception {
        mockMvc.perform(get("/operator/showComplaintsFromDrivers")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/showComplaintsFromDrivers"));
    }

    @Test
    void passengers() throws Exception {
        mockMvc.perform(get("/operator/passengers")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/passengers"));
    }

    @Test
    void drivers() throws Exception {
        mockMvc.perform(get("/operator/drivers")
                .with(user("o1").password("o1").roles("OPERATOR")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("operator/drivers"));
    }


    @Test
    void mainPassenger() throws Exception {
        mockMvc.perform(get("/passenger")
                .with(user("p1").password("p1").roles("PASSENGER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("passenger/mainPassenger"));
    }


    @Test
    void sendData() throws Exception {
        mockMvc.perform(get("/passenger/sendData")
                .with(user("p1").password("p1").roles("PASSENGER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("passenger/sendData"));
    }

    @Test
    void sendDataRedirect() throws Exception {
        mockMvc.perform(post("/passenger/sendData")
                .with(user("p1").password("p1").roles("PASSENGER"))
                .with(csrf())
                .param("microdistrictFrom", "microdistrictFrom")
                .param("streetFrom", "streetFrom")
                .param("microdistrictTo", "microdistrictTo")
                .param("streetTo", "streetTo")
                .param("category", DriverCategory.ECONOMY.toString()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/passenger"));
    }

    @Test
    void getConfirmedOrder() throws Exception {
        mockMvc.perform(get("/passenger/getConfirmedOrder")
                .with(user("p1").password("p1").roles("PASSENGER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("passenger/getConfirmedOrder"));
    }


    @Test
    void sendComplaintFromPassenger() throws Exception {
        mockMvc.perform(get("/passenger/sendComplaint")
                .with(user("p1").password("p1").roles("PASSENGER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("passenger/sendComplaint"));
    }

    @Test
    void mainDriver() throws Exception {
        mockMvc.perform(get("/driver")
                .with(user("d1").password("d1").roles("DRIVER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("driver/mainDriver"));
    }

    @Test
    void showOrder() throws Exception {
        mockMvc.perform(get("/driver/showOrder")
                .with(user("d1").password("d1").roles("DRIVER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("driver/showOrder"));
    }

    @Test
    void showOrderRedirect() throws Exception {
        mockMvc.perform(post("/driver/showOrder")
                .with(user("d1").password("d1").roles("DRIVER"))
                .with(csrf())
                .param("agree", "yes"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/driver"));
    }


    @Test
    void finishTrip() throws Exception {
        mockMvc.perform(get("/driver/finishTrip")
                .with(user("d1").password("d1").roles("DRIVER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("driver/finishTrip"));
    }

    @Test
    void sendComplaintFromDriver() throws Exception {
        mockMvc.perform(get("/driver/sendComplaint")
                .with(user("d1").password("d1").roles("DRIVER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("driver/sendComplaint"));
    }

    @Test
    void setStatus() throws Exception {
        mockMvc.perform(get("/driver/setStatus")
                .with(user("d1").password("d1").roles("DRIVER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("driver/setStatus"));
    }
}