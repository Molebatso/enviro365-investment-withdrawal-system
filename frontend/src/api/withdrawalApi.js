import httpClient from './httpClient';

/**
 * Withdrawal-related API calls.
 */

export const submitWithdrawal = (investorId, withdrawalRequest) => {
  return httpClient.post(`/investors/${investorId}/withdrawals`, withdrawalRequest);
};

export const getWithdrawalHistory = (investorId) => {
  return httpClient.get(`/investors/${investorId}/withdrawals`);
};

export const getExportCsvUrl = ({ investorId, status } = {}) => {
  const params = new URLSearchParams();
  if (investorId) params.append('investorId', investorId);
  if (status) params.append('status', status);
  const query = params.toString();
  return `http://localhost:8080/api/withdrawals/export${query ? `?${query}` : ''}`;
};
