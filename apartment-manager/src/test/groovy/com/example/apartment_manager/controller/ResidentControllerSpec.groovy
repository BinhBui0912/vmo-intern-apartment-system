package com.example.apartment_manager.controller


import com.example.apartment_manager.dto.request.ResidentRequest
import com.example.apartment_manager.entity.Apartment
import com.example.apartment_manager.entity.Resident
import com.example.apartment_manager.service.ResidentService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDate

class ResidentControllerSpec extends Specification {

    def residentService = Mock(ResidentService)
    def residentController = new ResidentController(residentService)

    def "getAllResidents should return paginated list of residents"() {
        given: "A pageable request and mocked service response"
        def pageable = PageRequest.of(0, 10, Sort.by("id").ascending())
        def apt = new Apartment(id: 1L)
        def residents = [
                new Resident(id : 1L, fullName: "Duy Binh", gender: "FEMALE", apartment: apt),
                new Resident(id:  2L, fullName:  "Binh Bui", gender: "MALE", apartment: apt)
        ]
        def page = new PageImpl<>(residents, pageable, residents.size())
        residentService.getAllResidents(pageable) >> page

        when:
        def response = residentController.getAllResidents(0, 10)

        then:
        response.statusCode == HttpStatus.OK
        response.body.code == 200
        response.body.message == "Get all residents"
        response.body.data.size() == 2
        response.body.data[0].id == 1L
        response.body.data[0].fullName == "Duy Binh"
        response.body.data[1].id == 2L
        response.body.data[1].fullName == "Binh Bui"
    }

    def "getResidentById should return resident when found"() {
        given:
        def apt = new Apartment(id: 1L)
        def resident = new Resident(id: 1L, fullName: "Duy Binh", gender: "MALE", apartment: apt)
        residentService.getResidentById(1L) >> resident

        when:
        def response = residentController.getResidentById(1L)

        then:
        response.body.code == 200
        response.body.message == "Get Resident by ID: 1 successfully"
        response.body.data.fullName == "Duy Binh"
    }

    def "createResident should create new resident successfully"() {
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
                id : 1,
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
        def response = residentController.createResident(request)

        then:
        response.body.code == 201
        response.body.message == "Resident created successfully"
        response.body.data.email == "binh@gmail.com"
    }

    def "updateResident should update existing resident successfully"() {
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
        def response = residentController.updateResident(id, request)

        then:
        response.body.code == 200
        response.body.message == "Resident updated successfully"
        response.body.data.email == "binhbo@gmail.com"
    }

    def "deleteResident should delete resident successfully"() {
        given:
        def id = 1L
        residentService.deleteResident(id) >> {}

        when:
        def response = residentController.deleteResident(id)

        then:
        response.body.code == 200
        response.body.message == "Resident deleted successfully"
        response.body.data == null
    }

    def "searchResidentsByName should be successfully"() {
        given:
        def searchName = "Binh"
        def pageable = PageRequest.of(0, 10, Sort.by("fullName").ascending())
        def apt = new Apartment(id: 1L)
        def residents = [
                new Resident(id : 1L, fullName: "Duy Binh", gender: "FEMALE", apartment: apt),
                new Resident(id:  2L, fullName:  "Binh Bui", gender: "MALE", apartment: apt)
        ]
        def page = new PageImpl<>(residents, pageable, residents.size())
        residentService.searchResidentsByName(searchName, pageable) >> page

        when:
        def response = residentController.searchResidentsByName(searchName, 0, 10)

        then:
        response.statusCode == HttpStatus.OK
        response.body.code == 200
        response.body.message == "Search Residents By Name: Binh successfully"
        response.body.data.size() == 2
        response.body.data.every { it.fullName.contains("Binh") }
    }
}
