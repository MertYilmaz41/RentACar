package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.promoCodeRequests.CreatePromoCodeRequest;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.UpdatePromoCodeRequest;
import com.btkAkademi.rentACar.core.utilities.results.Result;

public interface PromoCodeService {
	Result add(CreatePromoCodeRequest createPromoCodeRequest);
	Result update(UpdatePromoCodeRequest updatePromoCodeRequest);
}
