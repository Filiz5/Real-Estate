'use client';

import UserPageHeader from '@/components/common/UserPageHeader';
import styles from '@/styles/components/login/register-page.module.scss';
import Link from 'next/link';
import TextInput from '../common/form-fields/TextInput';
import { useFormState } from 'react-dom';
import { useRouter } from 'next/navigation';
import { swAlert } from '@/helpers/swal';
import { initialResponse } from '@/helpers/form-validation';
import { registerAction } from '@/actions/register-action';

export default function RegisterPage() {
  const [state, dispatch] = useFormState(registerAction, initialResponse);
  const router = useRouter();

  if (state.ok) {
    swAlert(state.message, 'success', '', 4000);
    router.push('/login');
  } else if (state.message) {
    swAlert(state.message, 'error', '', 4000);
  }

  return (
    <main className={styles.container}>
      <UserPageHeader text="REGISTER" />
      <div className={styles.form_container}>
        <form noValidate action={dispatch}>
          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="First Name"
            name="firstName"
            type="text"
            required
            error={state?.errors?.firstName} // Field-specific error
          />

          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="Last Name"
            name="lastName"
            type="text"
            required
            error={state?.errors?.lastName} // Field-specific error
          />

          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="Phone"
            name="phone"
            type="tel"
            required
            error={state?.errors?.phone} // Field-specific error
          />

          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="Email"
            name="email"
            type="email"
            required
            error={state?.errors?.email} // Field-specific error
          />

          <TextInput
            className="input_group"
            inputClassName="input_field"
            labelClassName="label_field"
            label="Password"
            name="password"
            type="password"
            passwordVisible={true}
            required
            error={state?.errors?.password} // Field-specific error
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
            REGISTER
          </button>
        </form>
        <div className={styles.login_link_container}>
          <p>
            Already have an account,{' '}
            <Link href="/login" className={styles.register_link}>
              Login now!
            </Link>
          </p>
        </div>
      </div>
    </main>
  );
}
