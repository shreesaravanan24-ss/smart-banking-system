package com.smartbanking.beneficiary.service;

import com.smartbanking.beneficiary.dto.BeneficiaryRequest;
import com.smartbanking.beneficiary.dto.BeneficiaryResponse;

import java.util.List;

public interface BeneficiaryService {

    BeneficiaryResponse addBeneficiary(
            BeneficiaryRequest request);

    List<BeneficiaryResponse> getCustomerBeneficiaries(
            Long customerId);

    void deleteBeneficiary(Long id);
}