"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { updateAdvertType } from '@/actions/advert-type-actions';
import Swal from 'sweetalert2';
import styles from '@/styles/components/admin/advert-type/advert-type-form.module.scss';

export default function UpdateAdvertTypeForm({ initialData, id }) {
  const [advertType, setAdvertType] = useState({
    title: initialData.title,
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAdvertType(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      await updateAdvertType(id, advertType);
      Swal.fire({
        icon: 'success',
        title: 'Success',
        text: 'Advert type updated successfully!',
      }).then(() => {
        router.push('/admin-dashboard/admin-advert-types');
      });
    } catch (err) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: err.message,
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className={styles.container}>
      <h1>Edit Advert Type</h1>
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.formGroup}>
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={advertType.title}
            onChange={handleChange}
            className={styles.input}
            disabled={isSubmitting}
          />
        </div>
        <div className={styles.buttonGroup}>
          <button 
            type="submit" 
            className={styles.submitButton}
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Saving...' : 'Save Changes'}
          </button>
          <button 
            type="button" 
            onClick={() => router.back()} 
            className={styles.cancelButton}
            disabled={isSubmitting}
          >
            Back
          </button>
        </div>
      </form>
    </div>
  );
} 