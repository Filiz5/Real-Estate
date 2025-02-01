"use client";

import styles from '@/styles/components/common/form-fields/text-input.module.scss';
import { useState } from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa';

const TextInput = ({
  className,
  labelClassName,
  inputClassName,
  label,
  error,
  readOnly,
  passwordVisible,
  onChange,
  existingValue,
  ...rest
}) => {
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);

  const togglePasswordVisibility = () => {
    setIsPasswordVisible((prevState) => !prevState);
  };
  return (
    <div className={`${styles[className]}`}>
      <label className={`${styles[labelClassName]}`}>{label}</label>
      <div className={`${passwordVisible ? styles['container'] : ''}`}>
        <input
          type={passwordVisible ? isPasswordVisible ? 'text' : 'password': rest.type}
          className={`${styles[inputClassName]} ${
            error ? styles['is-invalid'] : ''
          } ${passwordVisible ? styles['password_visible'] : ''}`}
          name={rest.name}
          readOnly={readOnly}
          defaultValue={rest.defaultValue ? rest.defaultValue : undefined}
          onChange={onChange || (() => {})}
        />
        {passwordVisible && (
          <button
            type="button"
            className={styles['show_password']}
            onClick={togglePasswordVisibility}
          >
            {isPasswordVisible ? <FaEye size={20}/> : <FaEyeSlash size={20} className={styles.FaEyeSlash}/>}
          </button>
        )}
      </div>

      {/* Display error message */}
      {error && <div className={styles['error-message']}>{error}</div>}
    </div>
  );
};

export default TextInput;
