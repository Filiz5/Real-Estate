'use client';

import UserPageHeader from '@/components/common/UserPageHeader';
import { loginAction } from '@/actions/auth-action';
import styles from '@/styles/components/login/login-page.module.scss';
import Link from 'next/link';
import TextInput from '../common/form-fields/TextInput';
import { useFormState } from 'react-dom';
import { initialResponse } from '@/helpers/form-validation';
import { AlertText } from '../common/AlertText';
import LoginWithGoogle from './LoginWithGoogle';

export default function LoginPage() {
  const [state, dispatch] = useFormState(loginAction, initialResponse);

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
            label="Email"
            name="email"
            type="email"
            required
            error={state?.errors?.email}
          />
          {/* Password Field */}
          <TextInput
            className="input_group"
            labelClassName="label_field"
            inputClassName="input_field"
            label="Password"
            name="password"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.password}
          />
          <div className={styles.forgot_password}>
            <Link href={'/login/forgot-password'}>Forgot password?</Link>
          </div>
          {/* Submit Button */}
          <button type="submit" className={styles.login_button}>
            LOGIN
          </button>
        </form>
        <LoginWithGoogle />
        <div className={styles.register_container}>
          <p>
            If you don’t have an account,{' '}
            <Link href="/login/register" className={styles.register_link}>
              Register now!
            </Link>
          </p>
        </div>
      </div>
    </main>
  );
}
