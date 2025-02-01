"use client"; 

import styles from '@/styles/components/admin/admin-adverts-edit/advert-fields.module.scss';
import PropertiesComponent from './PropertiesComponent';
import FieldsComponent from './FieldsComponent';
import { useEffect, useState } from 'react';
import { swAlert, swConfirm } from '@/helpers/swal';
import { deleteAdvert } from '@/services/advert-service';
import { useRouter } from 'next/navigation';
import { UpdateStatusAction } from '@/actions/advert-action';
import { set } from 'react-hook-form';
import Spinner from '@/components/ui/Spinner';
import MessageField from '@/components/ui/MessageFiled';

export default function AdvertFields({ advert, properties}) {
  const router = useRouter();
  const [isUpdating, setIsUpdating] = useState(false);
  const [status, setStatus] = useState('');
  const [existingStatus, setExistingStatus] = useState('');
  const [error, setError] = useState('');
  const [isMessageOpen, setIsMessageOpen] = useState(false); // Modal control state
  const [reason, setReason] = useState(''); // Reason input state
  const [fields, setFields] = useState({
    title: advert.title || 'N/A',
    desc: advert.desc || 'N/A',
    category: advert.category.title || 'N/A',
    type: advert.advertType.title || 'N/A',
    country: advert.country.name || 'N/A',
    city: advert.city.name || 'N/A',
    district: advert.district || 'N/A',
    location: advert.location || 'N/A',
    status: advert.advertStatus || 'N/A',
    price: advert.price || 'N/A'
  });

  useEffect(() => {
    setExistingStatus(advert.advertStatus);
  }, [advert]);


  const handleStasuChange = (status) => {
    setStatus(status);
  }

  const handleEdit = async () => {
    

    try {
      if (status === existingStatus) {
        setError('You cannot update the status to the same one');
        return;
      }

      if(status === 'PENDING' || status === 'REJECTED') {
        setIsMessageOpen(true);
        return;
      }

      await updateAdvertStatus();

    } catch (error) {
      swAlert(error.message, 'error');
    }
  }

  const updateAdvertStatus = async () => {
    const answer = await swConfirm('Do you confirm the changes?');
    if (!answer.isConfirmed) return;

    setIsUpdating(true);

    const statusInfo = { status: status, rejectMessage: reason }; // Include the reason
    const res = await UpdateStatusAction(advert.id, statusInfo);

    if (res.success) {
      setIsUpdating(false);
      swAlert(res.message, 'success');
      router.push('/admin-dashboard/admin-adverts');
    } else {
      swAlert(res.message, 'error');
      setIsUpdating(false);
    }
  };

  const handleModalSubmit = async () => {
    if (!reason.trim()) {
      swAlert('Please provide a reason', 'error');
      return;
    }
    setIsMessageOpen(false); // Close the modal
    await updateAdvertStatus(); // Proceed with updating
  };


  const handleDelete = async () => {
    // delete advert
    const answer = await swConfirm('Are you sure you want to delete this advert?');
            if(!answer.isConfirmed) return;
    
            const res = await deleteAdvert(advert.id);
    
            if(res.success) {
                swAlert(res.message, 'success');
                router.push('/admin-dashboard/admin-adverts');
            }else{
                swAlert(res.message, 'error');
            } 
  }

  return (
    <div className={styles.container}>
      <div className={styles.head}>
        <p className={styles.head_line}>Advert Information</p>
        <p className={styles.sub_head_line}></p>
      </div>
      <div className={styles.advert_container}>
        <div className={styles.fields_container}>
          <div className={styles.fields}>
            <FieldsComponent
              fields={fields}
              handleStasuChange={handleStasuChange}
              error={error}
            />
            <div className={styles.buttons_container}>
              <button
                className={styles.delete_button}
                onClick={() => {
                  handleDelete();
                }}
              >
                Delete
              </button>
              <button
                className={styles.edit_button}
                onClick={() => {
                  handleEdit();
                }}
              >
                {isUpdating ? (
                  <>
                    <Spinner size="small" /> Updating...
                  </>
                ) : (
                  'Update'
                )}
              </button>
            </div>
          </div>

          <PropertiesComponent properties={properties} />
        </div>
      </div>
      {isMessageOpen && (
        <MessageField onClose={() => setIsMessageOpen(false)}>
          <div className={styles.message_content}>
            <h2>Provide a Reason</h2>
            <textarea
              className={styles.message_textarea}
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              placeholder="Enter the reason for rejection or pending status"
              rows="5"
              style={{ width: '100%', padding: '10px' }}
            />
            <button
              className={styles.message_button}
              onClick={handleModalSubmit}
            >
              Submit
            </button>
          </div>
        </MessageField>
      )}
    </div>
  );
}
