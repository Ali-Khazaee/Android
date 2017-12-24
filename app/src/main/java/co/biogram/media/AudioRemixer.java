package co.biogram.media;

import java.nio.ShortBuffer;

interface AudioRemixer
{
    void remix(final ShortBuffer inSBuff, final ShortBuffer outSBuff);

    AudioRemixer DOWNMIX = new AudioRemixer()
    {
        private static final int SIGNED_SHORT_LIMIT = 32768;
        private static final int UNSIGNED_SHORT_MAX = 65535;

        @Override
        public void remix(final ShortBuffer inSBuff, final ShortBuffer outSBuff)
        {

            final int inRemaining = inSBuff.remaining() / 2;
            final int outSpace = outSBuff.remaining();
            final int samplesToBeProcessed = Math.min(inRemaining, outSpace);

            for (int i = 0; i < samplesToBeProcessed; ++i)
            {
                int m;
                final int a = inSBuff.get() + SIGNED_SHORT_LIMIT;
                final int b = inSBuff.get() + SIGNED_SHORT_LIMIT;

                if ((a < SIGNED_SHORT_LIMIT) || (b < SIGNED_SHORT_LIMIT))
                    m = a * b / SIGNED_SHORT_LIMIT;
                else
                    m = 2 * (a + b) - (a * b) / SIGNED_SHORT_LIMIT - UNSIGNED_SHORT_MAX;

                if (m == UNSIGNED_SHORT_MAX + 1)
                    m = UNSIGNED_SHORT_MAX;

                outSBuff.put((short) (m - SIGNED_SHORT_LIMIT));
            }
        }
    };

    AudioRemixer UPMIX = new AudioRemixer()
    {
        @Override
        public void remix(final ShortBuffer inSBuff, final ShortBuffer outSBuff)
        {
            final int inRemaining = inSBuff.remaining();
            final int outSpace = outSBuff.remaining() / 2;

            final int samplesToBeProcessed = Math.min(inRemaining, outSpace);
            for (int i = 0; i < samplesToBeProcessed; ++i)
            {
                final short inSample = inSBuff.get();
                outSBuff.put(inSample);
                outSBuff.put(inSample);
            }
        }
    };

    AudioRemixer PASSTHROUGH = new AudioRemixer()
    {
        @Override
        public void remix(final ShortBuffer inSBuff, final ShortBuffer outSBuff)
        {
            outSBuff.put(inSBuff);
        }
    };
}
