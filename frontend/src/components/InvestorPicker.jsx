import { useEffect, useState } from 'react';
import { getAllInvestors } from '../api/investorApi';
import './InvestorPicker.css';

/**
 * Since this assessment has no authentication, the investor picker
 * stands in for "who is logged in". Every page (Dashboard, Withdrawal
 * Form, History) reads the currently selected investor id from here.
 */
function InvestorPicker({ selectedInvestorId, onSelect }) {
  const [investors, setInvestors] = useState([]);
  const [status, setStatus] = useState('loading'); // loading | success | error

  useEffect(() => {
    getAllInvestors()
      .then((response) => {
        setInvestors(response.data);
        setStatus('success');
        if (response.data.length > 0 && !selectedInvestorId) {
          onSelect(response.data[0].id);
        }
      })
      .catch(() => setStatus('error'));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (status === 'loading') {
    return <div className="investor-picker investor-picker--loading">Loading investors…</div>;
  }

  if (status === 'error') {
    return (
      <div className="investor-picker investor-picker--error">
        Could not load investors. Is the backend running on port 8080?
      </div>
    );
  }

  return (
    <div className="investor-picker">
      <label htmlFor="investor-select">Investor</label>
      <select
        id="investor-select"
        value={selectedInvestorId ?? ''}
        onChange={(e) => onSelect(Number(e.target.value))}
      >
        {investors.map((investor) => (
          <option key={investor.id} value={investor.id}>
            {investor.firstName} {investor.lastName} (age {investor.age})
          </option>
        ))}
      </select>
    </div>
  );
}

export default InvestorPicker;
