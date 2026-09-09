import { useEffect, useState } from 'react';
import { getPortfolio } from '../../api/investorApi';
import { submitWithdrawal } from '../../api/withdrawalApi';
import { LoadingState, ErrorState } from '../common/StatusStates';
import './WithdrawalForm.css';

const WITHDRAWAL_TYPES = ['STANDARD', 'RETIREMENT'];

/**
 * Withdrawal Form.
 *
 * UI validation here is a convenience for the investor (catch obvious
 * mistakes before a round-trip) - it is NOT the source of truth. The
 * backend re-validates everything (amount positivity, balance, 90%
 * cap, retirement age) and its response is always what determines
 * success or failure.
 */
function WithdrawalForm({ investorId, onWithdrawalSuccess }) {
  const [portfolio, setPortfolio] = useState(null);
  const [loadStatus, setLoadStatus] = useState('loading'); // loading | success | error

  const [withdrawalType, setWithdrawalType] = useState('STANDARD');
  const [amount, setAmount] = useState('');
  const [formError, setFormError] = useState(null);

  const [submitState, setSubmitState] = useState('idle'); // idle | submitting | success | error
  const [submitMessage, setSubmitMessage] = useState('');

  useEffect(() => {
    if (!investorId) return;
    loadPortfolio();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [investorId]);

  function loadPortfolio() {
    setLoadStatus('loading');
    getPortfolio(investorId)
      .then((res) => {
        setPortfolio(res.data);
        setLoadStatus('success');
      })
      .catch(() => setLoadStatus('error'));
  }

  function validateForm() {
    if (!withdrawalType) {
      return 'Please select a withdrawal type.';
    }
    if (amount === '' || amount === null) {
      return 'Withdrawal amount is required.';
    }
    const numericAmount = Number(amount);
    if (Number.isNaN(numericAmount) || numericAmount <= 0) {
      return 'Withdrawal amount must be greater than zero.';
    }
    return null;
  }

  function handleSubmit(event) {
    event.preventDefault();

    const validationError = validateForm();
    if (validationError) {
      setFormError(validationError);
      setSubmitState('idle');
      return;
    }
    setFormError(null);
    setSubmitState('submitting');
    setSubmitMessage('');

    submitWithdrawal(investorId, { withdrawalType, amount: Number(amount) })
      .then((res) => {
        setSubmitState('success');
        setSubmitMessage('Withdrawal notice created successfully.');
        setAmount('');
        setPortfolio((prev) => ({ ...prev, availableBalance: res.data.remainingBalance }));
        if (onWithdrawalSuccess) onWithdrawalSuccess();
      })
      .catch((err) => {
        setSubmitState('error');
        const backendMessage = err.response?.data?.message;
        setSubmitMessage(backendMessage || 'The withdrawal request failed. Please try again.');
      });
  }

  if (loadStatus === 'loading') {
    return <LoadingState message="Loading balance…" />;
  }

  if (loadStatus === 'error') {
    return <ErrorState message="Could not load this investor's balance. Please try again." />;
  }

  const maxWithdrawal = (portfolio.availableBalance * 0.9).toFixed(2);

  return (
    <div className="withdrawal-form-card">
      <h2>Submit a Withdrawal</h2>

      <div className="withdrawal-balance-info">
        <span>Available balance: <strong>R{Number(portfolio.availableBalance).toLocaleString()}</strong></span>
        <span>Maximum allowed withdrawal (90%): <strong>R{Number(maxWithdrawal).toLocaleString()}</strong></span>
      </div>

      <form onSubmit={handleSubmit} noValidate>
        <div className="form-field">
          <label htmlFor="withdrawal-type">Withdrawal type</label>
          <select
            id="withdrawal-type"
            value={withdrawalType}
            onChange={(e) => setWithdrawalType(e.target.value)}
          >
            {WITHDRAWAL_TYPES.map((type) => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>

        <div className="form-field">
          <label htmlFor="withdrawal-amount">Amount (ZAR)</label>
          <input
            id="withdrawal-amount"
            type="number"
            step="0.01"
            min="0"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="e.g. 5000.00"
          />
        </div>

        {formError && <p className="form-message form-message--error">{formError}</p>}

        <button type="submit" disabled={submitState === 'submitting'}>
          {submitState === 'submitting' ? 'Submitting…' : 'Submit Withdrawal'}
        </button>

        {submitState === 'success' && (
          <p className="form-message form-message--success">{submitMessage}</p>
        )}
        {submitState === 'error' && (
          <p className="form-message form-message--error">{submitMessage}</p>
        )}
      </form>
    </div>
  );
}

export default WithdrawalForm;
