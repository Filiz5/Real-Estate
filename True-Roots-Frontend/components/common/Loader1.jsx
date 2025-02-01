
import { DotLottieReact } from '@lottiefiles/dotlottie-react';

export default function Loader() {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '40vh'
      }}
    >
      <DotLottieReact
        src={`/assets/animations/spinner.lottie`}
        style={{
          width: '300px', // Adjust the width
          height: '150px' // Adjust the height
        }}
        loop
        autoplay
        aria-expanded="true"
      />
    </div>
  );
   
};
