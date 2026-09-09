import { getExportCsvUrl } from '../../api/withdrawalApi';
import './CsvDownloadButton.css';

/**
 * A plain anchor tag (not a fetch/blob download) is used deliberately:
 * clicking a link is a normal browser navigation, so the backend's
 * Content-Disposition header drives the filename and save behaviour
 * without needing any extra JS plumbing.
 */
function CsvDownloadButton({ investorId, status }) {
  const href = getExportCsvUrl({ investorId, status });

  return (
    <a className="csv-download-button" href={href} download>
      Download CSV
    </a>
  );
}

export default CsvDownloadButton;
