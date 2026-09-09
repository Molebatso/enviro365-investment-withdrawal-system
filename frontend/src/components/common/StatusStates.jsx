import './StatusStates.css';

export function LoadingState({ message = 'Loading…' }) {
  return <div className="status-state status-state--loading">{message}</div>;
}

export function ErrorState({ message = 'Something went wrong. Please try again.' }) {
  return <div className="status-state status-state--error">{message}</div>;
}

export function EmptyState({ message = 'No records found.' }) {
  return <div className="status-state status-state--empty">{message}</div>;
}
