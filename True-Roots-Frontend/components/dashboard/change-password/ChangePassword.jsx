"use client";

import { changePasswordAction } from "@/actions/userAction";
import TextInput from "@/components/common/form-fields/TextInput";
import UserPageHeader from '@/components/common/UserPageHeader';
import { initialResponse } from "@/helpers/form-validation";
import styles from '@/styles/components/login/register-page.module.scss';
import { useFormState } from "react-dom";
import { swAlert } from "@/helpers/swal";
import { signOut } from 'next-auth/react';

export default function ChangePassword() {
    const [state, dispatch] = useFormState(changePasswordAction, initialResponse);

    if (state.ok) {
        swAlert(state.message, 'success', '', 4000);
        setTimeout(async() => {
          await signOut({ callbackUrl: '/login' });
        }, 2000);
      } else if (state.message) {
        swAlert(state.message, 'error', '', 4000);
      }

  return (
    <main className={styles.container}>
      <UserPageHeader text="CHANGE PASSWORD" />
      <div className={styles.form_container}>
        <form noValidate action={dispatch}>
          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="Current Password"
            name="oldPassword"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.oldPassword} // Field-specific error
          />

          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="New Password"
            name="newPassword"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.newPassword} // Field-specific error
          />

          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="Confirm Password"
            name="confirmPassword"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.confirmPassword} // Field-specific error
          />

          <button type="submit" className={styles.button}>
            Update
          </button>
        </form>
      </div>
    </main>
  );
}
