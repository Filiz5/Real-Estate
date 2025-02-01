import * as Yup from 'yup';

export const RegisterSchema = Yup.object({
  firstName: Yup.string()
    .matches(
      /^[A-Za-z\s]+$/,
      'Name cannot contain numbers or special characters'
    )
    .matches(/.*\S.*/, 'First name cannot consist entirely of whitespace')
    .max(30, 'First name must be at most 30 characters')
    .min(2, 'First name must be at least 2 characters')
    .required('First name field cannot be empty'),
  lastName: Yup.string()
    .matches(
      /^[A-Za-z\s]+$/,
      'Last name cannot contain numbers or special characters'
    )
    .matches(/.*\S.*/, 'First name cannot consist entirely of whitespace')
    .max(30, 'Last name must be at most 30 characters')
    .min(2, 'Last name must be at least 2 characters')
    .required('Last name field cannot be empty'),
  phone: Yup.string()
    .matches(
      /^((\(\d{3}\))|\d{3})[- .]?\d{3}[- .]?\d{4}$/,
      'Invalid phone number. It should be in the format 222-333-4444'
    )
    .required('Phone field cannot be empty'),
  email: Yup.string()
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\.[a-zA-Z]{2,}$/,
      'Invalid format! Example: `abc@xyz.com`'
    )
    .required('Email field cannot be empty'),
  password: Yup.string()
    .matches(
      /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@.!$!_()=#+/~'%*?&])[A-Za-z\d@.!$!_()=#+/~'%*?&]{8,}$/,
      'Password must contain at least8 characters, one uppercase, one lowercase, one number, and one special character.'
    )
    .matches(/^\S*$/, 'Password cannot contain spaces')
    .matches(/^(?!\s*$)/, 'Password cannot be empty or contain only whitespace')
    .min(8, 'Password must be at least 8 characters')
    .required('Password field cannot be empty'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password'), null], 'Passwords must match')
    .required('Confirm password field cannot be empty')
});

export const UpdateUserSchema = Yup.object({
  firstName: Yup.string()
    .matches(
      /^[A-Za-z\s]+$/,
      'Name cannot contain numbers or special characters'
    )
    .matches(/.*\S.*/, 'First name cannot consist entirely of whitespace')
    .max(30, 'First name must be at most 30 characters')
    .min(2, 'First name must be at least 2 characters')
    .required('First name field cannot be empty'),
  lastName: Yup.string()
    .matches(
      /^[A-Za-z\s]+$/,
      'Last name cannot contain numbers or special characters'
    )
    .matches(/.*\S.*/, 'First name cannot consist entirely of whitespace')
    .max(30, 'Last name must be at most 30 characters')
    .min(2, 'Last name must be at least 2 characters')
    .required('Last name field cannot be empty'),
  phone: Yup.string()
    .matches(
      /^((\(\d{3}\))|\d{3})[- .]?\d{3}[- .]?\d{4}$/,
      'Invalid phone number. It should be in the format 222-333-4444'
    )
    .required('Phone field cannot be empty'),
  email: Yup.string()
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\.[a-zA-Z]{2,}$/,
      'Invalid format! Example: `abc@xyz.com`'
    )
    .required('Email field cannot be empty')
});


export const ChangePasswordSchema = Yup.object({
  oldPassword: Yup.string()
    .min(8, 'Invalid password')
    .required('Current password field cannot be empty'),
  newPassword: Yup.string()
    .matches(
      /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@.!$!_()=#+/~'%*?&])[A-Za-z\d@.!$!_()=#+/~'%*?&]{8,}$/,
      'Password must contain at least8 characters, one uppercase, one lowercase, one number, and one special character.'
    )
    .matches(/^\S*$/, 'Password cannot contain spaces')
    .matches(/^(?!\s*$)/, 'Password cannot be empty or contain only whitespace')
    .min(8, 'Password must be at least 8 characters')
    .required('Password field cannot be empty'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('newPassword'), null], 'Passwords must match')
    .required('Password field cannot be empty')
});