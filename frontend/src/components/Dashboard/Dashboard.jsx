import { useEffect, useState } from 'react';
import { getInvestor } from '../../api/investorApi';
import { getPortfolio } from '../../api/investorApi';
import { LoadingState, ErrorState, EmptyState } from '../common/StatusStates';
import './Dashboard.css';

/**
 * Portfolio Dashboard: investor details, available balance, and the
 * list of investment products making up the portfolio.
 */
function Dashboard({ investorId }) {
  const [investor, setInvestor] = useState(null);
  const [portfolio, setPortfolio] = useState(null);
  const [status, setStatus] = useState('loading'); // loading | success | error

  useEffect(() => {
    if (!investorId) return;

    setStatus('loading');
    Promise.all([getInvestor(investorId), getPortfolio(investorId)])
      .then(([investorRes, portfolioRes]) => {
        setInvestor(investorRes.data);
        setPortfolio(portfolioRes.data);
        setStatus('success');
      })
      .catch(() => setStatus('error'));
  }, [investorId]);

  if (status === 'loading') {
    return <LoadingState message="Loading portfolio…" />;
  }

  if (status === 'error') {
    return <ErrorState message="Could not load this investor's portfolio. Please try again." />;
  }

  const maxWithdrawal = (portfolio.availableBalance * 0.9).toFixed(2);

  return (
    <div className="dashboard">
      <section className="dashboard-card">
        <h2>Investor</h2>
        <dl className="dashboard-details">
          <dt>Name</dt>
          <dd>{investor.firstName} {investor.lastName}</dd>
          <dt>Age</dt>
          <dd>{investor.age}</dd>
          <dt>Email</dt>
          <dd>{investor.email}</dd>
        </dl>
      </section>

      <section className="dashboard-card">
        <h2>Portfolio</h2>
        <dl className="dashboard-details">
          <dt>Available balance</dt>
          <dd className="dashboard-balance">R{Number(portfolio.availableBalance).toLocaleString()}</dd>
          <dt>Maximum withdrawal (90%)</dt>
          <dd>R{Number(maxWithdrawal).toLocaleString()}</dd>
        </dl>
      </section>

      <section className="dashboard-card">
        <h2>Investment Products</h2>
        {portfolio.investmentProducts.length === 0 ? (
          <EmptyState message="This investor has no investment products yet." />
        ) : (
          <table className="dashboard-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Current Value</th>
              </tr>
            </thead>
            <tbody>
              {portfolio.investmentProducts.map((product) => (
                <tr key={product.id}>
                  <td>{product.productName}</td>
                  <td>R{Number(product.currentValue).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}

export default Dashboard;
