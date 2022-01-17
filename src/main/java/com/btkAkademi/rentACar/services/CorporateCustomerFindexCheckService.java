package com.btkAkademi.rentACar.services;

import java.util.Random;

public class CorporateCustomerFindexCheckService {
	public int calculateFindexScoreEnough(String taxNumber) 
	{
		int maxScore = 1901;
		int minScore = 649;
		Random random = new Random();
		int findexScore = random.nextInt(minScore,maxScore);
		System.out.println("Findex Score : " + findexScore);
		return findexScore;
	}
	
}
