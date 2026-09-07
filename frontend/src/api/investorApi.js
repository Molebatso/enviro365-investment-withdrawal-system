import httpClient from './httpClient';

/**
 * Investor-related API calls.
 * Implemented against the real backend endpoints in Phase 10
 * (Integration) once GET /api/investors/{id} and
 * GET /api/investors/{id}/portfolio exist.
 */

export const getInvestor = (investorId) => {
  return httpClient.get(`/investors/${investorId}`);
};

export const getPortfolio = (investorId) => {
  return httpClient.get(`/investors/${investorId}/portfolio`);
};
