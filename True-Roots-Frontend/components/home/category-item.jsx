import { RadioButton } from 'primereact/radiobutton';
import styles from '@/styles/components/home/Category.module.scss';

const CategoryItem = ({ label, value, isChecked, onChange }) => {
  return (
    <div className={styles.categoryItem}>
      <RadioButton
        inputId={value}
        name="category"
        value={value}
        checked={isChecked} // Seçili durumu
        onChange={onChange} // State'i güncelle
        className={styles.customRadioButton}
      />
      <label
        htmlFor={value}
        className={`${styles.label} ${isChecked ? styles.activeLabel : ''}`}
      >
        {label}
      </label>
    </div>
  );
};

export default CategoryItem;
