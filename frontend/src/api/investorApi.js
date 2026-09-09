import httpClient from './httpClient';

/**
 * Investor-related API calls.
 */

export const getAllInvestors = () => {
  return httpClient.get('/investors');
};

export const getInvestor = (investorId) => {
  return httpClient.get(`/investors/${investorId}`);
};

export const getPortfolio = (investorId) => {
  return httpClient.get(`/investors/${investorId}/portfolio`);
};
