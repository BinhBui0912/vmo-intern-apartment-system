package com.example.apartment_manager.controller


import com.example.apartment_manager.dto.request.ResidentRequest
import com.example.apartment_manager.entity.Apartment
import com.example.apartment_manager.entity.Resident
import com.example.apartment_manager.service.ResidentService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = ResidentController, 
    excludeAutoConfiguration = [SecurityAutoConfiguration, SecurityFilterAutoConfiguration])
@ContextConfiguration(classes = [ResidentController, MockConfig])
class ResidentControllerIT extends Specification {

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        @Primary
        ResidentService residentService() {
            return detachedMockFactory.Mock(ResidentService)
        }
    }

    @Autowired
    MockMvc mockMvc

    @Autowired
    ResidentService residentService

    @Autowired
    ObjectMapper objectMapper

    def "GET /api/v1/residents should return paginated residents list"() {
        given:
        def apartment = new Apartment(id : 1L)
        def residents = [
                new Resident(id : 1L, fullName: "Duy Binh", gender: "FEMALE", apartment: apartment),
                new Resident(id:  2L, fullName:  "Binh Bui", gender: "MALE", apartment: apartment)
        ]
        def page = new PageImpl<>(residents)
        residentService.getAllResidents(_ as Pageable) >> page

        when:
        def result = mockMvc.perform(get("/api/v1/residents")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.code').value(200))
                .andExpect(jsonPath('$.message').value("Get all residents"))
                .andExpect(jsonPath('$.data').isArray())
                .andExpect(jsonPath('$.data.length()').value(2))
                .andExpect(jsonPath('$.data[0].id').value(1))
                .andExpect(jsonPath('$.data[0].fullName').value("Duy Binh"))
                .andExpect(jsonPath('$.data[1].id').value(2))
                .andExpect(jsonPath('$.data[1].fullName').value("Binh Bui"))
    }

    def "GET /api/v1/residents/{id} should return specific resident"() {
        given:
        def apartment = new Apartment(id : 1L)
        def resident = new Resident(id : 1L, fullName: "Duy Binh", gender: "FEMALE", apartment: apartment)
        residentService.getResidentById(1L) >> resident

        when:
        def result = mockMvc.perform (get("/api/v1/residents/1")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.code').value(200))
                .andExpect(jsonPath('$.message').value("Get Resident by ID: 1 successfully"))
                .andExpect(jsonPath('$.data.id').value(1))
                .andExpect(jsonPath('$.data.fullName').value("Duy Binh"))
    }

    def "POST /api/v1/residents should create new resident"() {
        given:
        def apt = new Apartment(id : 1L)
        def request = new ResidentRequest(
                fullName: "Binh",
                email: "binh@gmail.com",
                phoneNumber: "0123456789",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                apartmentId: 1L
        )
        def resident = new Resident(
                id: 1,
                fullName: "Binh",
                email: "binh@gmail.com",
                phoneNumber: "0123456789",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                apartment: apt
        )
        residentService.createResident(request) >> resident

        when:
        def result = mockMvc.perform(post("/api/v1/residents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        result.andExpect(status().isCreated())
                .andExpect(jsonPath('$.code').value(201))
                .andExpect(jsonPath('$.message').value("Resident created successfully"))
                .andExpect(jsonPath('$.data.id').value(1))
                .andExpect(jsonPath('$.data.fullName').value("Binh"))
    }

    def "POST /api/v1/residents with invalid data should return validation error"() {
        given:
        def invalidRequest = new ResidentRequest()

        when:
        def result = mockMvc.perform(post("/api/v1/residents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))

        then:
        result.andExpect(status().isBadRequest())
    }

    def "PUT /api/v1/residents/{id} should update existing resident"() {
        given:
        def id = 1L
        def apt = new Apartment(id : 1L)
        def request = new ResidentRequest(
                fullName: "Duy Binh",
                email: "binhbo@gmail.com",
                phoneNumber: "0123456789",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                apartmentId: 1L
        )
        def resident = new Resident(
                id: 1L,
                fullName: "Duy Binh",
                email: "binhbo@gmail.com",
                phoneNumber: "0123456789",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                apartment: apt
        )
        residentService.updateResident(id, request) >> resident

        when:
        def result = mockMvc.perform(put("/api/v1/residents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$.code').value(200))
                .andExpect(jsonPath('$.message').value("Resident updated successfully"))
                .andExpect(jsonPath('$.data.id').value(1))
                .andExpect(jsonPath('$.data.fullName').value("Duy Binh"))
    }

    def "DELETE /api/v1/residents/{id} should delete resident"() {
        given:
        residentService.deleteResident(1L) >> {}

        when:
        def result = mockMvc.perform(delete("/api/v1/residents/1")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$.code').value(200))
                .andExpect(jsonPath('$.message').value("Resident deleted successfully"))
                .andExpect(jsonPath('$.data').isEmpty())
    }

    def "GET /api/v1/residents/search should return filtered residents"() {
        given:
        def apartment = new Apartment(id: 1L)
        def searchName = "Binh"
        def residents = [
                new Resident(id : 1L, fullName: "Duy Binh", gender: "FEMALE", apartment: apartment),
                new Resident(id:  2L, fullName:  "Binh Bui", gender: "MALE", apartment: apartment)
        ]
        def page = new PageImpl<>(residents)
        residentService.searchResidentsByName(searchName, _ as Pageable) >> page

        when:
        def result = mockMvc.perform(get("/api/v1/residents/search")
                .param("name", searchName)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$.code').value(200))
                .andExpect(jsonPath('$.message').value("Search Residents By Name: Binh successfully"))
                .andExpect(jsonPath('$.data').isArray())
                .andExpect(jsonPath('$.data.length()').value(2))
                .andExpect(jsonPath('$.data[0].fullName').value("Duy Binh"))
                .andExpect(jsonPath('$.data[1].fullName').value("Binh Bui"))
    }
}
