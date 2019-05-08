for file in ../src/main/resources/bardAssets/images/ui/topPanel/bard/{1..4}.png; do
	convert $file -modulate 100,50 ${file%.*}d.png
done