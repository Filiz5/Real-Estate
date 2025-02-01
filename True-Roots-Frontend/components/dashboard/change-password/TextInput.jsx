import styles from '@/styles/components/dashboards/change-password/change-password.module.scss';

const TextInput = ({
  className,
  labelClassName,
  inputClassName,
  label,
  error,
  readOnly,
  onChange,
  existingValue,
  ...rest
}) => {
  return (
    <div className={`${styles[className]}`}>
      <label className={`${styles[labelClassName]}`}>{label}</label>
      <input
        className={`${styles[inputClassName]} ${
          error ? styles['is-invalid'] : ''
        }`}
        {...rest}
        readOnly={readOnly}
        onChange={onChange || (() => {})}
      />
      {/* Display error message */}
      {error && <div className={styles['error-message']}>{error}</div>}
    </div>
  );
};

export default TextInput;
