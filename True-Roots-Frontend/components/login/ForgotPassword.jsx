"use client";

import { initialResponse } from '@/helpers/form-validation';
import styles from '@/styles/components/login/forgot-password.module.scss';
import { useFormState } from 'react-dom';
import TextInput from '../common/form-fields/TextInput';
import { forgotPasswordAction } from '@/actions/auth-action';
import UserPageHeader from '@/components/common/UserPageHeader';
import { useRouter } from 'next/navigation';
import { swAlert } from '@/helpers/swal';
import { AlertText } from '../common/AlertText';

export default function ForgotPassword() {
  const router = useRouter();
  const [state, dispatch] = useFormState(forgotPasswordAction, initialResponse);

  if(state.ok){
    swAlert("Reset code is sent. Please check your email account.", 'success');
    router.push('/login/reset-password');
  }
  return (
    <main className={styles.container}>
      <UserPageHeader text="FORGOT PASSWORD" />
      <form noValidate action={dispatch} className={styles.group_container}>
        {state.message && <AlertText type="error" text={state.message} />}
        <TextInput
          className="input_group"
          labelClassName="label_field"
          inputClassName="input_field"
          label="Email"
          name="email"
          type="email"
          required
          error={state?.errors?.email}
        />
        <button className={styles.button}>SEND RESET CODE</button>
      </form>
    </main>
  );
}
