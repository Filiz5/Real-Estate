'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { createAdvertType } from '@/actions/advert-type-actions';
import Swal from 'sweetalert2';
import styles from '@/styles/components/admin/advert-type/advert-type-form.module.scss';
import { swAlert } from '@/helpers/swal';
import { wait } from '@/utils/wait';
import Spinner from '@/components/ui/Spinner';

export default function AddAdvertType() {
  const [advertType, setAdvertType] = useState({
    title: ''
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const router = useRouter();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAdvertType((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const data = await createAdvertType(advertType);
      await wait(2000);
      swAlert(data.message, 'success');
      router.push('/admin-dashboard/admin-advert-types');
    } catch (err) {
      swAlert(err.message, 'error');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className={styles.container}>
      <h1>Add New Advert Type</h1>
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
            required
          />
        </div>
        <div className={styles.buttonGroup}>
          <button
            type="submit"
            className={styles.submitButton}
            disabled={isSubmitting}
          >
            {isSubmitting ? (
              <span className={styles.buttonContent}>
                <Spinner size="small" color="white" />
                <span className={styles.buttonText}>Creating...</span>
              </span>
            ) : (
              'Create'
            )}
          </button>
          <button
            type="button"
            onClick={() => router.back()}
            className={styles.cancelButton}
            disabled={isSubmitting}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
