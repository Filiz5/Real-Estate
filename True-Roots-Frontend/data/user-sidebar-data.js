import { TbLockPassword } from "react-icons/tb";
import { ImProfile } from 'react-icons/im';
import { FaBullhorn } from "react-icons/fa";
import { IoAddCircle } from "react-icons/io5";
import { MdFavorite, MdTour } from "react-icons/md";

// const TbLockPassword = dynamic(() => import("react-icons/tb").then(mod => mod.TbLockPassword), { ssr: false });
// const ImProfile = dynamic(() => import('react-icons/im').then(mod => mod.ImProfile), { ssr: false });
// const FaBullhorn = dynamic(() => import("react-icons/fa").then(mod => mod.FaBullhorn), { ssr: false });
// const IoAddCircle = dynamic(() => import("react-icons/io5").then(mod => mod.IoAddCircle), { ssr: false });
// const MdFavorite = dynamic(() => import("react-icons/md").then(mod => mod.MdFavorite), { ssr: false });
// const MdTour = dynamic(() => import("react-icons/md").then(mod => mod.MdTour), { ssr: false });

export const userSidebarData = [
  {
    _id: '1',
    pathname: '/dashboard/profile-page',
    title: 'My Profile',
    icon: <ImProfile />
  },
  {
    _id: '2',
    pathname: '/dashboard/change-password',
    title: 'Change Password',
    icon: <TbLockPassword />
  },
  {
    _id: '3',
    pathname: '/dashboard/my-adverts',
    title: 'My Adverts',
    icon: <FaBullhorn />
  },
  {
    _id: '4',
    pathname: '/dashboard/add-new-advert',
    title: 'Add Property',
    icon: <IoAddCircle />
  },
  {
    _id: '5',
    pathname: '/dashboard/tour-request',
    title: 'Tour Requests',
    icon: <MdTour />
  },
  {
    _id: '6',
    pathname: '/dashboard/my-favorites',
    title: 'My Favorites',
    icon: <MdFavorite />
  }
];
