import styles from '@/styles/components/common/login/login-header.module.scss';

export default function LoginHeader({ text }) {
  return <div className={styles.login_container}>{text}</div>;
}
