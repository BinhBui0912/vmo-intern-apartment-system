package com.example.apartment_manager.serivce

import com.example.apartment_manager.dto.request.ResidentRequest
import com.example.apartment_manager.entity.Apartment
import com.example.apartment_manager.entity.Resident
import com.example.apartment_manager.exception.DataNotFoundException
import com.example.apartment_manager.repository.ApartmentRepository
import com.example.apartment_manager.repository.ResidentRepository
import com.example.apartment_manager.service.Impl.ResidentServiceImpl
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDate

class ResidentServiceImplSpec extends Specification{
    def residentRepository = Mock(ResidentRepository)
    def apartmentRepository = Mock(ApartmentRepository)
    def residentService = new ResidentServiceImpl(residentRepository, apartmentRepository)

    def "getAllResidents should return paginated list"(){
        given:
        def pageable = PageRequest.of(0, 10);
        def residents = List.of(new Resident(id: 1L), new Resident(id: 2L))
        def page = new PageImpl<>(residents)
        residentRepository.findAll(pageable) >> page

        when:
        def result = residentService.getAllResidents(pageable)
        then:
        result.getContent().size() == 2
    }

    def "getResidentById should return resident if exist"(){
        given:
        def resident = new Resident(id : 1L)
        residentRepository.findById(1L) >> Optional.of(resident)

        when:
        def result = residentService.getResidentById(1L)

        then:
        result == resident
    }

    def "getResidentById should throw exception if not found"(){
        given:
        residentRepository.findById(1L) >> Optional.empty()

        when:
        residentService.getResidentById(1L)

        then:
        def ex = thrown(DataNotFoundException)
        ex.message.contains("Resident not found with id: 1")
    }

    def "createResident should succeed with valid request"(){
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
        1 * apartmentRepository.findById(1L) >> Optional.of(apt)
        1 * residentRepository.existsByEmail(_) >> false
        1 * residentRepository.existsByPhoneNumber(_) >> false
        1 * residentRepository.existsByIdentityNumber(_) >> false
        1 * residentRepository.existsByApartmentAndIsRepresentativeTrue(_) >> false
        1 * residentRepository.save(_) >> {args -> args[0]}

        when:
        def result = residentService.createResident(request)

        then:
        result.fullName == "Binh"
        result.isRepresentative == true
    }

    def "createResident should throw Exception if email exist"(){
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
        1 * apartmentRepository.findById(1L) >> Optional.of(apt)
        1 * residentRepository.existsByEmail(_) >> true

        when:
        def result = residentService.createResident(request)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.getMessage().contains("Email already exists: binh@gmail.com")
    }

    def "deleteResident should mark as inactive"(){
        given:
        def resident = new Resident(id : 1L, isActive: true)
        residentRepository.findById(1L) >> Optional.of(resident)

        when:
        residentService.deleteResident(1L)

        then:
        resident.isActive == false
    }

    def "updateResident should success"(){
        given:
        def id = 1L
        def apt = new Apartment(id : 1L)
        def resident = new Resident(
                id: id,
                fullName: "Binh",
                email: "binh@gmail.com",
                phoneNumber: "0123456789",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                isActive: true,
                apartment: apt
        )
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
        1 * apartmentRepository.findById(1L) >> Optional.of(apt)
        1 * residentRepository.findById(1L) >> Optional.of(resident)
        1 * residentRepository.existsByApartmentAndIsRepresentativeTrueAndIdNot(_, _) >> false
        1 * residentRepository.existsByEmailAndIdNot(_, _) >> false
        1 * residentRepository.existsByPhoneNumberAndIdNot(_, _) >> false
        1 * residentRepository.existsByIdentityNumberAndIdNot(_, _) >> false
        1 * residentRepository.save(_) >> {args -> args[0]}


        when:
        def result = residentService.updateResident(id, request)

        then:
        result.fullName == "Duy Binh"
        result.email == "binhbo@gmail.com"
        result.isRepresentative == true
    }

    def "updateResident shout throw Exception if PhoneNumber exist"(){
        given:
        def id = 1L
        def apt = new Apartment(id : 1L)
        def resident = new Resident(
                id: id,
                fullName: "Binh",
                email: "binh@gmail.com",
                phoneNumber: "0123456789",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                isActive: true,
                apartment: apt
        )
        def request = new ResidentRequest(
                fullName: "Duy Binh",
                email: "binhbo@gmail.com",
                phoneNumber: "012345678",
                identityNumber: "123456789",
                dateOfBirth: LocalDate.of(2003, 12, 9),
                gender: "MALE",
                isRepresentative: true,
                apartmentId: 1L
        )
        1 * apartmentRepository.findById(1L) >> Optional.of(apt)
        1 * residentRepository.findById(1L) >> Optional.of(resident)
        1 * residentRepository.existsByApartmentAndIsRepresentativeTrueAndIdNot(_, _) >> false
        1 * residentRepository.existsByEmailAndIdNot(_, _) >> false
        1 * residentRepository.existsByPhoneNumberAndIdNot(_, _) >> true


        when:
        def result = residentService.updateResident(id, request)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.getMessage().contains("Phone number already exists: 012345678")
    }

    def "searchResidentByName should return residents matching given name"(){
        given:
        def pageable = PageRequest.of(0, 10)
        def name = "binh"
        def residents = [
                new Resident(id: 1L, fullName: "Bui Binh"),
                new Resident(id: 2L, fullName: "Duy Binh")
        ]
        def page = new PageImpl<>(residents)
        residentRepository.findAll(_, pageable) >> page

        when:
        def result = residentService.searchResidentsByName(name, pageable)

        then:
        result.getTotalElements() == 2
    }

    def "searchResidentsByName should return empty page if name is blank or null"() {
        given:
        def pageable = PageRequest.of(0, 10)

        when:
        def resultBlank = residentService.searchResidentsByName("", pageable)
        def resultNull = residentService.searchResidentsByName(null, pageable)

        then:
        resultBlank.isEmpty()
        resultNull.isEmpty()
    }
}