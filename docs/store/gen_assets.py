from PIL import Image, ImageDraw, ImageFont
import os

out = os.path.dirname(os.path.abspath(__file__))
FONT = 'C:/Windows/Fonts/malgun.ttf'
FONT_B = 'C:/Windows/Fonts/malgunbd.ttf'

def font(size, bold=False):
    try:
        return ImageFont.truetype(FONT_B if bold else FONT, size)
    except:
        return ImageFont.load_default()

# 1. icon-512.png
img = Image.new('RGBA', (512, 512), (139, 26, 26, 255))
draw = ImageDraw.Draw(img)
for i in range(200, 0, -1):
    c = (min(255, 139 + int(i * 0.3)), min(255, 26 + int(i * 0.2)), 26, 255)
    draw.ellipse([256 - i, 256 - i, 256 + i, 256 + i], fill=c)
draw.ellipse([156, 156, 356, 356], fill=(212, 165, 116, 255))
draw.ellipse([176, 176, 336, 336], fill=(139, 26, 26, 255))
draw.ellipse([231, 231, 281, 281], fill=(205, 164, 52, 255))
draw.text((256, 370), '사주', fill=(255, 255, 255, 255), font=font(48, True), anchor='mt')
draw.text((256, 435), '운세', fill=(205, 164, 52, 255), font=font(40, True), anchor='mt')
img.save(os.path.join(out, 'icon-512.png'))
print('icon-512.png created')

# 2. feature-1024x500.png
feat = Image.new('RGB', (1024, 500), (139, 26, 26))
fd = ImageDraw.Draw(feat)
for cx, cy, r in [(200, 250, 120), (824, 250, 120), (512, 250, 160)]:
    fd.ellipse([cx - r, cy - r, cx + r, cy + r], fill=(100, 10, 10))
    fd.ellipse([cx - r + 20, cy - r + 20, cx + r - 20, cy + r - 20], fill=(139, 26, 26))
fd.text((512, 180), '사주와운세', fill=(255, 255, 255), font=font(72, True), anchor='mt')
fd.text((512, 280), '사주팔자 \xb7 오늘의 운세 \xb7 띄별운세 \xb7 궁합',
        fill=(212, 165, 116), font=font(32), anchor='mt')
fd.text((512, 340), '당신의 운명을 알아보세요',
        fill=(255, 255, 255), font=font(28), anchor='mt')
feat.save(os.path.join(out, 'feature-1024x500.png'))
print('feature-1024x500.png created')

# Verify
for f, w, h in [('icon-512.png', 512, 512), ('feature-1024x500.png', 1024, 500)]:
    im = Image.open(os.path.join(out, f))
    assert im.size == (w, h), f'{f} size mismatch: {im.size}'
    print(f'{f} verified: {im.size}, mode={im.mode}')
