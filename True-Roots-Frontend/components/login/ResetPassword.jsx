"use client";

import UserPageHeader from '@/components/common/UserPageHeader';
import styles from '@/styles/components/login/reset-password.module.scss';
import { AlertText } from '../common/AlertText';
import TextInput from '../common/form-fields/TextInput';
import { initialResponse } from '@/helpers/form-validation';
import { useFormState } from 'react-dom';
import { resetPasswordAction } from '@/actions/auth-action';
import { useRouter } from 'next/navigation';
import { swAlert } from '@/helpers/swal';

export default function ResetPassword() {
  const router = useRouter();
  const [state, dispatch] = useFormState(resetPasswordAction, initialResponse);

  if (state.ok) {
    swAlert(state.message, 'success');
    router.push('/login');
  }

  return (
    <main className={styles.container}>
      <UserPageHeader text="LOGIN" />
      <div className={styles.group_container}>
        <form action={dispatch} className={styles.form}>
          {/* Email Field */}
          {state.message && <AlertText type="error" text={state.message} />}
          <TextInput
            className="input_group"
            labelClassName="label_field"
            inputClassName="input_field"
            label="Reset Code"
            name="code"
            type="text"
            required
            error={state?.errors?.code}
          />
          {/* Password Field */}
          <TextInput
            className="input_group"
            labelClassName="label_field"
            inputClassName="input_field"
            label="New Password"
            name="password"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.password}
          />

          <TextInput
            className="input_group"
            labelClassName="label_field"
            inputClassName="input_field"
            label="Confirm New Password"
            name="confirmPassword"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.confirmPassword}
          />

          <button type="submit" className={styles.login_button}>
            RESET PASSWORD
          </button>
        </form>
      </div>
    </main>
  );
}
