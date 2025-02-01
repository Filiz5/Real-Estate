import { signIn } from 'next-auth/react';
import styles from '@/styles/components/login/login-with-google.module.scss';
import { FcGoogle } from 'react-icons/fc';
import { swAlert } from '@/helpers/swal';
import { loginWithGoogleAction } from '@/actions/auth-action';
import { AlertText } from '../common/AlertText';
import { useFormState } from 'react-dom';
import { initialResponse } from '@/helpers/form-validation';

export default function LoginWithGoogle() {
  const [state, dispatch] = useFormState(loginWithGoogleAction, initialResponse);

  const handleLogin = async () => {
   
      dispatch();	
    }


  return (
    <>
      {state.message && <AlertText type="error" text={state.message} />}
      <div className={styles.container}>
        <hr className={styles.hr} />
        <p className={styles.or}>or</p>
        <p className={styles.text}>Login with</p>
        <button
          onClick={() => handleLogin()} // Use NextAuth's signIn method
          className={styles.google_button}
        >
          <FcGoogle size={35} className={styles.icon} /> <span>Google</span>
        </button>
      </div>
    </>
  );
}
