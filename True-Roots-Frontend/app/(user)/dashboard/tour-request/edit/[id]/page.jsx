import UserPageHeader from '@/components/common/UserPageHeader';
import {TourRequestDetail} from '@/components/dashboard/user-tour-request/tour-request-detail';
import { getTourRequestDetailForUser } from '@/services/getTourRequestDetailForUser-service';

export default async function UpdateTourRequests({ params }) {
  const res = await getTourRequestDetailForUser(params.id);


  return (
    <div>
      <UserPageHeader text="MY TOUR REQUESTS" />
      <TourRequestDetail res={res} />
    </div>
  );
}
