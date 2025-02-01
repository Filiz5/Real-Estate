"use client";

import UserPageHeader from '@/components/common/UserPageHeader'
import styles from '@/styles/components/dashboards/add-new-advert/create-advert.module.scss'
import AddressFields from './advert-fields/AddressFields';
import CommonInfoFields from './advert-fields/CommonInfoFields';
import PropertyFields from './advert-fields/PropertyFields';
import ImageFields from './advert-fields/ImageFields';
import { useEffect, useState } from 'react';
import { useFormState } from 'react-dom';
import { createAdvertAction } from '@/actions/advert-action';
import { convertFormDataToJSON, initialResponse } from '@/helpers/form-validation';
import { swAlert } from '@/helpers/swal';
import { useRouter } from 'next/navigation';
import Spinner from '@/components/ui/Spinner';

export default function CreateAdvert() {
  const router = useRouter();
  const [state, dispatch] = useFormState(createAdvertAction, initialResponse);
  const [images, setImages] = useState([]); // Store images here
  const [properties, setProperties] = useState([]);
  const [creating, setCreating] = useState(false);

  const handleImageChange = (uploadedImages) => {
    setImages(uploadedImages); // Update images when they are uploaded
  };

  const handlePropertyChange = (propertyData) => {
    setProperties(propertyData); // Update properties when they are changed
  };

  useEffect(() => {
    if(state.ok){
    setCreating(false);
    swAlert(state.message, 'success');
    router.push('/dashboard/my-adverts');
  }else if(state.message){
    setCreating(false);
    swAlert(state.message, 'error');
  }else if(state.errors){
    setCreating(false);
  }
  }, [state.ok, state.message, state.errors, router]);



  const handleSubmit = (event) => {
    setCreating(true);
    event.preventDefault();
    const form = event.target.closest('form'); // Get the closest form element
    const formData = new FormData(form); // Create a FormData object
    const advertData = convertFormDataToJSON(formData); // Convert the FormData to JSON
    if (advertData.price) {
      advertData.price = advertData.price.replace(/\./g, ''); // Remove dots
      advertData.price = parseFloat(advertData.price); // Convert to number
    }
    advertData.images = images; // Add the images to the advert data
    advertData.properties = properties; // Add the properties to the advert data
    console.log(advertData);
    
    
    dispatch(advertData); // Dispatch the data to the action
  };
  
  

  
  return (
    <main className={styles.main_container}>
      <UserPageHeader text="NEW ADVERT" />
      <div className={styles.container}>
        <form className={styles.form_container}>
          <CommonInfoFields state={state} />
          <AddressFields state={state} />
          <PropertyFields
            onPropertyChange={handlePropertyChange}
            state={state}
          />
          <ImageFields onImageChange={handleImageChange} state={state} />
          <div className={styles.button_container}>
            <button className={styles.cancel_button} onClick={() => router.push('/dashboard/my-adverts')}>
              Cancel
            </button>
            <button
            type="submit"
            className={styles.submit_button}
            onClick={handleSubmit}
          >
            {creating ? (
              <span>
                <Spinner color={'white'}/> {'Creating...'}
              </span>
            ) : (
              'Create'
            )}
          </button>
          </div>
          
        </form>
      </div>
    </main>
  );
}
