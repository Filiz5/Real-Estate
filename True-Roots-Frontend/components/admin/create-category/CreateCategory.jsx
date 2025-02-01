'use client';

import styles from '@/styles/components/admin/category/create-category.module.scss';
import Properties from './Properties';
import { useEffect, useState } from 'react';
import { useFormState } from 'react-dom';
import { useRouter } from 'next/navigation';
import {
  convertFormDataToJSON,
  initialResponse
} from '@/helpers/form-validation';
import { createCategoryAction } from '@/actions/category-action';
import { swAlert } from '@/helpers/swal';
import Spinner from '@/components/ui/Spinner';

export default function CreateCategory() {
  const [isActive, setIsActive] = useState(true);
  const router = useRouter();
  const [state, dispatch] = useFormState(createCategoryAction, initialResponse);
  const [propertyKeyRequests, setProperties] = useState([]);
  const [isCreating, setIsCreating] = useState(false);
  const handleProperties = (addedProperties) => {
    setProperties(addedProperties);
  };

  const toggleStatus = (e) => {
    e.preventDefault();
    setIsActive((prevStatus) => !prevStatus); // Invert the current status
  };


  const handleSubmit = (event) => {
    setIsCreating(true);
    event.preventDefault();
    const form = event.target.closest('form'); // Get the closest form element
    const formData = new FormData(form); // Create a FormData object
    const categoryData = convertFormDataToJSON(formData); // Convert the FormData to JSON
    const filteredProperties = propertyKeyRequests.map(
      ({ id, ...rest }) => rest
    );
    categoryData.propertyKeyRequests = filteredProperties; // Add the properties to the category data
    categoryData.status = isActive; // Add the status to the category data
    dispatch(categoryData); // Dispatch the data to the action
  };

    useEffect(() => {
      if (state.ok) {
        setIsCreating(false);
        swAlert(state.message, 'success');
        router.push('/admin-dashboard/admin-categories');
      } else if (state.message) {
        setIsCreating(false);
        swAlert(state.message, 'error');
      } else if (state.errors) {
        setIsCreating(false);
      }
    }, [state, router]);

  const handleBack = (e) => {
    e.preventDefault();
    router.push('/admin-dashboard/admin-categories');
  };

  return (
    <main className={styles.container}>
      <form className={styles.form}>
        <div className={styles.fields_container}>
          <div className={styles.title}>
            <label htmlFor="title">Title</label>
            <input type="text" id="title" name="title" />
            {state?.errors?.title && ( // Display the error message if there is one
              <span className={styles.error}>{state.errors.title}</span>
            )}
          </div>
          <div className={styles.slug}>
            <label htmlFor="slug">Slug</label>
            <input type="text" id="slug" name="slug" />
            {state?.errors?.slug && ( // Display the error message if there is one
              <span className={styles.error}>{state.errors.slug}</span>
            )}
          </div>
          <div className={styles.multiple_field}>
            <div className={styles.icon}>
              <div>
                <label htmlFor="icon">Icon</label>
                <input
                  type="icon"
                  id="icon"
                  name="icon"
                  placeholder="icon"
                />
              </div>

              {state?.errors?.icon && ( // Display the error message if there is one
                <span className={styles.error}>{state.errors.icon}</span>
              )}
            </div>
            <div className={styles.sequence}>
              <label htmlFor="seq">Sequence</label>
              <input type="number" id="seq" name="seq" />
              {state?.errors?.sequence && ( // Display the error message if there is one
                <span className={styles.error}>{state.errors.seq}</span>
              )}
            </div>
            <div className={styles.status}>
              <span>Status</span>
              <div className={styles.button_active}>
                <button
                  className={styles.active_button}
                  onClick={toggleStatus}
                  style={{ backgroundColor: isActive ? 'green' : 'gray' }}
                >
                  <div
                    className={styles.status_circle}
                    style={{ left: isActive ? '28px' : '-4px' }}
                  ></div>
                </button>
                <span className={styles.isActive_span}>
                  {isActive ? 'Active' : 'Inactive'}
                </span>
              </div>
            </div>
          </div>
          <div className={styles.create_container}>
            <button
              className={styles.create_button}
              type="button"
              onClick={handleBack}
            >
              Back
            </button>
            <button className={styles.create_button} onClick={handleSubmit}>
              {isCreating ? (
                <span>
                  <Spinner color={'white'} /> {'Creating...'}
                </span>
              ) : (
                'Create'
              )}
            </button>
          </div>
        </div>
        <div className={styles.properties_container}>
          <Properties handleProperties={handleProperties} state={state} />
        </div>
      </form>
    </main>
  );
}
