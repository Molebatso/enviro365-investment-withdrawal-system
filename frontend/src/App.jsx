import { useState } from 'react';
import InvestorPicker from './components/InvestorPicker';
import Dashboard from './components/Dashboard/Dashboard';
import WithdrawalForm from './components/WithdrawalForm/WithdrawalForm';
import WithdrawalHistory from './components/WithdrawalHistory/WithdrawalHistory';
import './App.css';

const TABS = [
  { key: 'dashboard', label: 'Dashboard' },
  { key: 'withdrawals', label: 'Withdrawals' },
  { key: 'history', label: 'History' },
];

/**
 * Top-level app shell: header, investor picker, and simple tab-based
 * navigation between the three pages. A router library (react-router)
 * was deliberately left out - three fixed, non-nested tabs with no
 * need for deep-linkable URLs don't justify the extra dependency.
 */
function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [selectedInvestorId, setSelectedInvestorId] = useState(null);

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Enviro365 Investments</h1>
        <nav>
          {TABS.map((tab) => (
            <button
              key={tab.key}
              className={`nav-link ${activeTab === tab.key ? 'nav-link--active' : ''}`}
              onClick={() => setActiveTab(tab.key)}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </header>

      <InvestorPicker selectedInvestorId={selectedInvestorId} onSelect={setSelectedInvestorId} />

      <main className="app-main">
        {!selectedInvestorId ? (
          <p>Select an investor to get started.</p>
        ) : (
          <>
            {activeTab === 'dashboard' && <Dashboard investorId={selectedInvestorId} />}
            {activeTab === 'withdrawals' && (
              <WithdrawalForm
                investorId={selectedInvestorId}
                onWithdrawalSuccess={() => setActiveTab('history')}
              />
            )}
            {activeTab === 'history' && <WithdrawalHistory investorId={selectedInvestorId} />}
          </>
        )}
      </main>
    </div>
  );
}

export default App;
