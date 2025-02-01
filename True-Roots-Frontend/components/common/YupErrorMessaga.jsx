

import styles from '@/styles/components/common/yup-error-message.module.scss';

export default function YupErrorMessaga({error}) {
  return (
    <div className={styles.container}>
        <p>{error}</p>
    </div>
  )
}
