import './App.css';

/**
 * Temporary placeholder shell.
 *
 * This confirms the frontend project runs and can reach the backend
 * health check. The real Dashboard / Withdrawal Form / History pages
 * are built in Phase 9 of the roadmap, once the backend APIs exist.
 */
function App() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Enviro365 Investments</h1>
        <nav>
          <span>Dashboard</span>
          <span>Withdrawals</span>
          <span>History</span>
        </nav>
      </header>

      <main className="app-main">
        <p>
          Frontend scaffold is running. Backend integration and real pages
          are added in later phases.
        </p>
      </main>
    </div>
  );
}

export default App;
