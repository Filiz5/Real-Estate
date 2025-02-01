import * as Yup from 'yup';

export const contactValidationSchema = Yup.object().shape({
  firstName: Yup.string()
    .trim()
    .matches(/^[a-zA-ZçÇğĞıİöÖşŞüÜ\s-]+$/, 'First name can only contain letters, spaces, and hyphens.') // Sadece harfler, boşluk ve tireler kabul edilir
    .min(2, 'First name must be between 2 and 30 characters.') // Minimum 2 karakter
    .max(30, 'First name must be between 2 and 30 characters.') // Maksimum 30 karakter
    .required('Please enter your first name.'), // Boş bırakılamaz

  lastName: Yup.string()
    .trim()
    .matches(/^[a-zA-ZçÇğĞıİöÖşŞüÜ\s-]+$/, 'Last name can only contain letters, spaces, and hyphens.') // Sadece harfler, boşluk ve tireler kabul edilir
    .min(2, 'Last name must be between 2 and 30 characters.')
    .max(30, 'Last name must be between 2 and 30 characters.')
    .required('Please enter your last name.'),

  email: Yup.string()
    .trim()
    .matches(
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      'Please enter a valid email address.'
    )
    .required('Please enter your email address.'),

  message: Yup.string()
    .trim()
    .matches(
      /^(?=.*[a-zA-Z])(?=.*\d)?(?=.*[\s\S]).{5,500}$/,
      'Message must include at least one letter and meaningful content. Only symbols or numbers are not allowed.'
    ) // Mesajda en az bir harf ve anlamlı içerik olmalı
    .min(5, 'Message must be between 5 and 500 characters.')
    .max(500, 'Message must be between 5 and 500 characters.')
    .required('Please enter your message.'),

  isHuman: Yup.boolean()
    .oneOf([true], 'Please confirm you are not a robot.') // Seçim zorunlu
});
