import UserPageHeader from '@/components/common/UserPageHeader';
import Profile from '@/components/dashboard/profile/Profile';
import ErrorPage from '@/errors/ErrorPage';
import { fetchUser } from '@/services/user-service';
import { wait } from '@/utils/wait';

export default async function ProfilePage() {

  try {

    const user = await fetchUser(); // Try fetching user data
    await wait(2000); // Simulate loading time
     return (
       <>
         <UserPageHeader text="MY PROFILE" />
           <Profile user={user} />
       </>
     );
  } catch (err) {
    return(
      <>
        <UserPageHeader text="MY PROFILE" />
        <ErrorPage message={err.message} />
      </>
    )
    
  }
 
}
