import styles from '@/styles/components/ui/message-field.module.scss';

export default function Modal({ children, onClose }) {
  return (
    <div className={styles.modal_backdrop}>
      <div className={styles.modal_content}>
        <button className={styles.close_button} onClick={onClose}>
          &times;
        </button>
        {children}
      </div>
    </div>
  );
}
