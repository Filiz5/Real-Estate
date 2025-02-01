import styles from '@/styles/components/admin/admin-adverts-edit/tour-requests.module.scss';
import { IoTrashBinOutline } from 'react-icons/io5';

export default function TourRequests({ tourRequests }) {
  return (
    <div className={styles.container}>
      <div className={styles.head}>
        <p className={styles.head_line}>Tour Requests</p>
        <p className={styles.sub_head_line}></p>
      </div>

      <div className={styles.table_container}>
        <table>
          <thead className={styles.table_head}>
            <tr>
              <th>Guest</th>
              <th>Status</th>
              <th>Tour Reguest Date</th>
              <th>Tour Request Time</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody className={styles.table_body}>
            {Array.isArray(tourRequests) && tourRequests.length > 0 ? (
              tourRequests.map((request, index) => (
                <tr key={index}>
                  <td>{`${request.guestFirstName} ${request.guestLastName}`}</td>
                  <td>
                    <span className={styles.status}>{request.tourRequestStatus}</span>
                  </td>
                  <td>{new Date(request.tourDate).toLocaleDateString()}</td>
                  <td>{new Date(request.tourDate).toLocaleTimeString()}</td>
                  <td>
                    <button className={styles.delete_button}>
                      <IoTrashBinOutline size={20} />
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5">No tour requests</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
