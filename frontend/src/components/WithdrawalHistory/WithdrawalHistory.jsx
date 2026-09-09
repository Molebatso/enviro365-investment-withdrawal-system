import { useEffect, useState } from 'react';
import { getWithdrawalHistory } from '../../api/withdrawalApi';
import { LoadingState, ErrorState, EmptyState } from '../common/StatusStates';
import CsvDownloadButton from './CsvDownloadButton';
import './WithdrawalHistory.css';

const STATUS_FILTERS = ['ALL', 'APPROVED', 'REJECTED'];

function WithdrawalHistory({ investorId }) {
  const [withdrawals, setWithdrawals] = useState([]);
  const [status, setStatus] = useState('loading'); // loading | success | error
  const [statusFilter, setStatusFilter] = useState('ALL');

  useEffect(() => {
    if (!investorId) return;

    setStatus('loading');
    getWithdrawalHistory(investorId)
      .then((res) => {
        setWithdrawals(res.data);
        setStatus('success');
      })
      .catch(() => setStatus('error'));
  }, [investorId]);

  if (status === 'loading') {
    return <LoadingState message="Loading withdrawal history…" />;
  }

  if (status === 'error') {
    return <ErrorState message="Could not load withdrawal history. Please try again." />;
  }

  const filteredWithdrawals = statusFilter === 'ALL'
    ? withdrawals
    : withdrawals.filter((w) => w.status === statusFilter);

  return (
    <div className="history-card">
      <div className="history-header">
        <h2>Withdrawal History</h2>
        <div className="history-controls">
          <label htmlFor="status-filter">Filter by status</label>
          <select
            id="status-filter"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            {STATUS_FILTERS.map((option) => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
          <CsvDownloadButton
            investorId={investorId}
            status={statusFilter === 'ALL' ? undefined : statusFilter}
          />
        </div>
      </div>

      {filteredWithdrawals.length === 0 ? (
        <EmptyState message="No withdrawal records match this filter." />
      ) : (
        <table className="history-table">
          <thead>
            <tr>
              <th>Withdrawal ID</th>
              <th>Date</th>
              <th>Type</th>
              <th>Amount</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {filteredWithdrawals.map((w) => (
              <tr key={w.id}>
                <td>{w.id}</td>
                <td>{new Date(w.requestedDate).toLocaleString()}</td>
                <td>{w.withdrawalType}</td>
                <td>R{Number(w.amount).toLocaleString()}</td>
                <td>
                  <span className={`status-badge status-badge--${w.status.toLowerCase()}`}>
                    {w.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default WithdrawalHistory;
