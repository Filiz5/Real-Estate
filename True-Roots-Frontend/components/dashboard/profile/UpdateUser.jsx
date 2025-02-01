"use client";

import TextInput from '@/components/common/form-fields/TextInput';
import { useFormState } from 'react-dom';
import { initialResponse } from '@/helpers/form-validation';
import { swAlert, swConfirm } from '@/helpers/swal';
import {updateUserAction } from '@/actions/userAction';
import styles from '@/styles/components/dashboards/profile/profile.module.scss';
import { useRouter } from 'next/navigation';
import { signOut } from 'next-auth/react';
import { useEffect, useState } from 'react';

export default function UpdateUser({ user }) {
    const router = useRouter();
    const { email, firstName, lastName, phone, builtIn } = user.object;
    const [state, dispatch] = useFormState(updateUserAction, initialResponse);
    const [data, setData] = useState(null);

    useEffect(() => {
      setData(user?.object||null);
    }, [user]);
    
    useEffect(() => {
      if (state.ok) {
        const updatedEmail = state.data?.email; // Extract updated email if available
        const updatedName = state.data?.firstName; // Extract updated name if available
        const updatedLastname = state.data?.lastName; // Extract updated phone if available
        if (email !== updatedEmail || firstName !== updatedName || lastName !== updatedLastname) {
          swAlert(
            'Because of changing personal information, your session is ended. Please login again',
            'info',
          );
          setTimeout(async () => {
            await signOut({ callbackUrl: '/login' });
          }, 4000);
        } else {
          swAlert(state.message, 'success', '', 4000);
          router.refresh();
        }
      } else if (state.message) {
        swAlert(state.message, 'error', '', 4000);
      }
    }, [state, email, router, firstName, lastName]);

    const handleConfirm = async (event) => {
      event.preventDefault(); // Prevent form submission

      // Show confirmation dialog
      const answer = await swConfirm(
        'Are you sure you want to update your information?',
        'warning',
        '',
        'Proceed',
        'No'
      );
      if (!answer.isConfirmed) return;

      // Collect form data
      const form = event.target.closest('form'); // Get the form element
      const formData = new FormData(form);

      // Dispatch the form data
      dispatch(formData);
    };
     

     
  return (
    <div className={styles.group_container}>
      <form className={styles.form}>
        {}
        {/* Email Field */}
        <TextInput
          className="input_group"
          labelClassName="label_field"
          inputClassName="input_field"
          label="Email"
          name="email"
          type="email"
          defaultValue={data?.email}
          error={state.errors?.email}
          disabled={builtIn}
        />
        {/* First Name Field */}
        <TextInput
          className="input_group"
          labelClassName="label_field"
          inputClassName="input_field"
          label="First Name"
          name="firstName"
          type="text"
          defaultValue={data?.firstName}
          error={state.errors?.firstName}
          disabled={builtIn}
        />
        {/* Last Name Field */}
        <TextInput
          className="input_group"
          labelClassName="label_field"
          inputClassName="input_field"
          label="Last Name"
          name="lastName"
          type="text"
          defaultValue={data?.lastName}
          error={state.errors?.lastName}
          disabled={builtIn}
        />
        {/* Phone Field */}
        <TextInput
          className="input_group"
          labelClassName="label_field"
          inputClassName="input_field"
          label="Phone"
          name="phone"
          type="text"
          defaultValue={data?.phone}
          error={state.errors?.phone}
          disabled={builtIn}
        />
        {/* Submit Button */}
        <button
          type="submit"
          className={styles.update_button}
          onClick={handleConfirm}
          disabled={builtIn}
        >
          UPDATE
        </button>
      </form>
    </div>
  );
}
