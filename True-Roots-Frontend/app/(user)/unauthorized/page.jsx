

import React from "react";
import UnAuthorized from "@/components/errors/unauthorized/Unauthorized";
import UserPageHeader from "@/components/common/UserPageHeader";

const UnAuthorizedPage = () => {
	return (
		<>
			<UserPageHeader text={"NOT FOUND"} />
			<UnAuthorized />
		</>
	);
};

export default UnAuthorizedPage;
