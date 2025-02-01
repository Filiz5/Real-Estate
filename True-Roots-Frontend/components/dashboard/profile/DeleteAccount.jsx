

import { swAlert, swConfirm } from '@/helpers/swal';
import { deleteUserAccountAction } from '@/actions/userAction';
import { useRouter } from 'next/navigation';
import { signOut } from 'next-auth/react';
import styles from '@/styles/components/dashboards/profile/profile.module.scss';

export default function DeleteAccount({ user }) {
    const { builtIn } = user.object;
      const router = useRouter();

      const handleDelete = async () => {
        const answer = await swConfirm(
          'Are you sure you want to delete your account?',
          'warning',
          '',
          'Proceed'
        );
        if (!answer.isConfirmed) return;
        const lastanswer = await swConfirm(
          'If you delete your account, all related records with this account will also be deleted permanently.',
          'warning'
        );
        if (!lastanswer.isConfirmed) return;

        const res = await deleteUserAccountAction();

        if (res.ok) {
          swAlert(res.message, 'success', '', 4000);
          setTimeout(async () => {
            await signOut({ callbackUrl: '/' });
          }, 4000);
        } else {
          swAlert(res.message, 'error', '', 4000);
        }
      };

  return (
    <div className={styles.delete_container}>
      {builtIn ? (
        <p className={styles.p}>You cannot delete your acount</p>
      ) : (
        <button type="submit" className={styles.delete} onClick={handleDelete}>
          Delete Account
        </button>
      )}
    </div>
  );
}
