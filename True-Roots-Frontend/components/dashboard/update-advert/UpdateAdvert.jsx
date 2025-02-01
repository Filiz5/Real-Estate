"use client";

import UserPageHeader from '@/components/common/UserPageHeader'
import styles from '@/styles/components/dashboards/update-advert/update-advert.module.scss'
import AddressFields from './advert-fields/AddressFields';
import CommonInfoFields from './advert-fields/CommonInfoFields';
import PropertyFields from './advert-fields/PropertyFields';
import UpdateImageFields from './advert-fields/UpdateImageFields';
import UploadImageFields from './advert-fields/UploadImageFields';
import { useEffect, useState } from 'react';
import { useFormState } from 'react-dom';
import { UpdateAdvertAction } from '@/actions/advert-action';
import { convertFormDataToJSON, initialResponse } from '@/helpers/form-validation';
import { swAlert } from '@/helpers/swal';
import { useRouter } from 'next/navigation';
import Spinner from '@/components/ui/Spinner';
import TourRequestsForOwner from './advert-fields/TourRequestsForOwner';

export default function CreateAdvert({ advert }) {
  const router = useRouter();
  const [state, dispatch] = useFormState(UpdateAdvertAction, initialResponse);
  const [properties, setProperties] = useState([]);
  const [updating, setUpdating] = useState(false);


  const handlePropertyChange = (propertyData) => {
    setProperties(propertyData); // Update properties when they are changed
  };

  useEffect(() => {
      if(state.ok){
      setUpdating(false);
      swAlert(state.message, 'success');
      router.push('/dashboard/my-adverts');
    }else if(state.message){
      setUpdating(false);
      swAlert(state.message, 'error');
    }else if(state.errors){
      setUpdating(false);
    }
    }, [state.ok, state.message, state.errors, router]);




  const handleSubmit = (event) => {
    setUpdating(true);
    event.preventDefault();
    const form = event.target.closest('form'); // Get the closest form element
    const formData = new FormData(form); // Create a FormData object
    let advertData = convertFormDataToJSON(formData); // Convert the FormData to JSON
    advertData.properties = properties; // Add the properties to the advert data
    const advertId = {advertId: advert?.id};
    advertData = {...advertData, ...advertId};
    
    
    dispatch(advertData); // Dispatch the data to the action
  };

  const handleCancel = (e) => {
    e.preventDefault();
    router.push('/dashboard/my-adverts');
  };
  return (
    <main className={styles.main_container}>
      <UserPageHeader text="UPDATE ADVERT" />
      <div className={styles.container}>
        <form className={styles.form_container}>
          <CommonInfoFields state={state} advert={advert} />
          <AddressFields state={state} advert={advert} />
          <PropertyFields
            onPropertyChange={handlePropertyChange}
            state={state}
            advert={advert}
          />
          <div>
            <button
              className={styles.cancel_button}
              onClick={(e) => handleCancel(e)}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={styles.submit_button}
              onClick={handleSubmit}
            >
              {updating ? (
                <span>
                  <Spinner color={'white'} /> {'Updating...'}
                </span>
              ) : (
                'Update'
              )}
            </button>
          </div>
        </form>

        <UploadImageFields advertId={advert?.id} />

        <UpdateImageFields advert={advert} />
        <TourRequestsForOwner data={advert?.tourRequests || []} />
      </div>
    </main>
  );
}
