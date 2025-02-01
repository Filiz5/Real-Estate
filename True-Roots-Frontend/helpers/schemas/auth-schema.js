import * as Yup from "yup";


export const AuthSchema = Yup.object({
  email: Yup.string()
    .required('Email field cannot be empty')
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]{3,}\.[a-zA-Z]{2,}$/,
      'Invalid format'
    ),
  password: Yup.string()
    .required('Password field connot be empty')
    .min(6, 'Password must be at least 8 characters long')
});

export const ForgotPasswordSchema = Yup.object({
  email: Yup.string()
    .required('Email field cannot be empty')
    .email('Invalid email address')
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]{3,}\.[a-zA-Z]{2,}$/,
      'Invalid format'
    )
});

export const ResetPasswordSchema = Yup.object({
  code: Yup.string().required('Reset code cannot be empty'),
  password: Yup.string()
    .min(8, 'Password must be at least 8 characters')
    .required('Password field cannot be empty')
    .matches(
      /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@.!$!_()=#+/~'%*?&])[A-Za-z\d@.!$!_()=#+/~'%*?&]{8,}$/,
      'Password must contain at least 8 characters, one uppercase, one lowercase, one number, and one special character.'
    )
    .matches(/^\S*$/, 'Password cannot contain spaces')
    .matches(
      /^(?!\s*$)/,
      'Password cannot be empty or contain only whitespace'
    ),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password'), null], 'Passwords must match')
    .required('Password field cannot be empty')
});
