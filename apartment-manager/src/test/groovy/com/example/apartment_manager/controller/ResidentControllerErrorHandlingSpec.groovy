package com.example.apartment_manager.controller


import com.example.apartment_manager.dto.request.ResidentRequest
import com.example.apartment_manager.exception.DataNotFoundException
import com.example.apartment_manager.service.ResidentService

import spock.lang.Specification

import java.time.LocalDate


class ResidentControllerErrorHandlingSpec extends Specification {

    def residentService = Mock(ResidentService)
    def residentController = new ResidentController(residentService)

    def "getResidentById should handle DataNotFoundException"() {
        given:
        residentService.getResidentById(999L) >> {
            throw new DataNotFoundException("Resident not found")
        }
        when:
        residentController.getResidentById(999L)

        then:
        thrown(DataNotFoundException)
    }

    def "createResident should handle service exceptions"() {
        given:
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
        residentService.createResident(request) >> {
            throw new IllegalArgumentException("Email already exists: binhbo@gmail.com")
        }

        when:
        residentController.createResident(request)

        then:
        thrown(IllegalArgumentException)
    }

    def "updateResident should handle DataNotFoundException"() {
        given:
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
        residentService.updateResident(999L, request) >> {
            throw new DataNotFoundException("Resident not found with id: 999")
        }

        when:
        residentController.updateResident(999L, request)

        then:
        thrown(DataNotFoundException)
    }

    def "deleteResident should handle DataNotFoundException"() {
        given:
        residentService.deleteResident(999L) >> {
            throw new DataNotFoundException("Resident not found")
        }

        when:
        residentController.deleteResident(999L)

        then:
        thrown(DataNotFoundException)
    }
}
