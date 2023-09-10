type CustomMetric = {
  id: string;
  name: string;
  value: number;
};

type ReportWebVitalsFunction = (
    onPerfEntry?: (metric: CustomMetric) => void
) => void;

const reportWebVitals: ReportWebVitalsFunction = (onPerfEntry) => {
  if (onPerfEntry && typeof onPerfEntry === 'function') {
    import('web-vitals').then(({ getCLS, getFID, getFCP, getLCP, getTTFB }) => {
      getCLS((metric) => onPerfEntry({ id: 'CLS', name: 'CumulativeLayoutShift', value: metric.value }));
      getFID((metric) => onPerfEntry({ id: 'FID', name: 'FirstInputDelay', value: metric.value }));
      getFCP((metric) => onPerfEntry({ id: 'FCP', name: 'FirstContentfulPaint', value: metric.value }));
      getLCP((metric) => onPerfEntry({ id: 'LCP', name: 'LargestContentfulPaint', value: metric.value }));
      getTTFB((metric) => onPerfEntry({ id: 'TTFB', name: 'TimeToFirstByte', value: metric.value }));
    });
  }
};

export default reportWebVitals;
