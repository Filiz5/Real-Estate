export const config = {
  api: {
    baseUrl: process.env.NEXT_PUBLIC_API_URL
  },
  checkUrlAndRole: [
    //Admin pages
    {
      urlRegex: /\/admin-dashboard$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-adverts$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-adverts\/edit\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-advert-types$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-advert-types\/new$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-advert-types\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-categories$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-categories\/new$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-categories\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-contact-messages$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex:
        /\/admin-dashboard\/admin-contact-messages\/adminContactMessageDetails\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-reports$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-settings$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-tour-requests$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-tour-requests\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-users$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-users\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/admin-dashboard\/admin-users\/advert\/[0-9]+$/,
      roles: ['ADMIN', 'MANAGER']
    },
    //Customer pages
    {
      urlRegex: /\/dashboard$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/profile-page$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/add-new-advert$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/change-password$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/my-adverts$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/my-adverts\/edit\/[0-9]+$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/tour-request$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/tour-request\/edit\/[0-9]+$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    },
    {
      urlRegex: /\/dashboard\/my-favorites$/,
      roles: ['CUSTOMER', 'ADMIN', 'MANAGER']
    }
  ]
};