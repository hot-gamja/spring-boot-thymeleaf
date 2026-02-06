/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: [
    './src/main/resources/templates/**/*.html',
    './src/main/resources/static/js/**/*.js',
  ],
  theme: {
    extend: {
      maxWidth: {
        'shell': '56rem',
      },
      colors: {
        surface: {
          DEFAULT: '#fafafa',
          dark: '#0a0a0a',
        },
        muted: {
          DEFAULT: '#6b7280',
          dark: '#9ca3af',
        },
        accent: {
          DEFAULT: '#3b82f6',
          dark: '#60a5fa',
        },
      },
      fontFamily: {
        sans: [
          'Inter', 'ui-sans-serif', 'system-ui', '-apple-system',
          'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'Helvetica Neue',
          'Arial', 'sans-serif',
        ],
        mono: [
          'ui-monospace', 'SFMono-Regular', 'Menlo', 'Monaco',
          'Consolas', 'Liberation Mono', 'Courier New', 'monospace',
        ],
      },
      typography: ({ theme }) => ({
        DEFAULT: {
          css: {
            '--tw-prose-body': theme('colors.zinc.700'),
            '--tw-prose-headings': theme('colors.zinc.900'),
            '--tw-prose-links': theme('colors.blue.600'),
            '--tw-prose-code': theme('colors.zinc.800'),
            '--tw-prose-pre-bg': theme('colors.zinc.50'),
            '--tw-prose-pre-code': theme('colors.zinc.800'),
            maxWidth: '75ch',
            lineHeight: '1.75',
            'h1': { fontSize: '2em', fontWeight: '700', marginTop: '0', marginBottom: '0.75em' },
            'h2': { fontSize: '1.5em', fontWeight: '600', marginTop: '1.75em', marginBottom: '0.5em' },
            'h3': { fontSize: '1.25em', fontWeight: '600', marginTop: '1.5em', marginBottom: '0.5em' },
            'h4': { fontSize: '1.1em', fontWeight: '600', marginTop: '1.25em', marginBottom: '0.5em' },
          },
        },
        invert: {
          css: {
            '--tw-prose-body': theme('colors.zinc.300'),
            '--tw-prose-headings': theme('colors.zinc.100'),
            '--tw-prose-links': theme('colors.blue.400'),
            '--tw-prose-code': theme('colors.zinc.200'),
            '--tw-prose-pre-bg': theme('colors.zinc.800/50'),
            '--tw-prose-pre-code': theme('colors.zinc.200'),
          },
        },
      }),
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
  ],
};
